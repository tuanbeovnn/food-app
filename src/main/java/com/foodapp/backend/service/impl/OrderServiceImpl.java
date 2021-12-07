package com.foodapp.backend.service.impl;


import com.foodapp.backend.dto.OrderDTO;
import com.foodapp.backend.dto.request.OrderDetailRequest;
import com.foodapp.backend.dto.response.OrderBuilderResponse;
import com.foodapp.backend.dto.response.OrderResponse;
import com.foodapp.backend.entity.CustomerEntity;
import com.foodapp.backend.entity.OrderDetailsEntity;
import com.foodapp.backend.entity.OrderEntity;
import com.foodapp.backend.entity.ProductEntity;
import com.foodapp.backend.exceptions.AppException;
import com.foodapp.backend.exceptions.CommonUtils;
import com.foodapp.backend.exceptions.CustomException;
import com.foodapp.backend.exceptions.ErrorCode;
import com.foodapp.backend.repository.CustomerRepository;
import com.foodapp.backend.repository.OrderDetailRepository;
import com.foodapp.backend.repository.ProductRepository;
import com.foodapp.backend.service.OrderRepository;
import com.foodapp.backend.service.OrderService;
import com.foodapp.backend.utils.Converter;
import com.foodapp.backend.utils.DateTimeUtils;
import com.foodapp.backend.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private static final String EMPTY = "";
    private static final String NOTIFY_URL = "https://callback.url/notify";
    private static final String RETURN_URL = "https://momo.vn/return";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final int MAX_PERCENTAGE = 100;

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderDetailRepository orderDetailRepository,
                            CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = Converter.toModel(orderDTO, OrderEntity.class);
        orderEntity.setOrderDate(DateTimeUtils.getDateTimeNow());

        CustomerEntity customerEntity = customerRepository.findById(SecurityUtils.getPrincipal().getUserId())
                .orElseThrow(() -> new CustomException("Not found user by " + SecurityUtils.getPrincipal().getUserId()));
        orderEntity.setCustomer(customerEntity);

        List<Long> productIds = orderDTO.getOrderDetailRequests().stream()
                .map(OrderDetailRequest::getProductId)
                .collect(Collectors.toList());
        List<ProductEntity> listProductsExists = getListProductsByIds(productIds);

        List<OrderDetailsEntity> orderDetailsEntities = new ArrayList<>();

        for (OrderDetailRequest orderDetailRequest : orderDTO.getOrderDetailRequests()) {

            Optional<ProductEntity> productEntity = listProductsExists.stream()
                    .filter(product -> Objects.equals(product.getId(), orderDetailRequest.getProductId()))
                    .findFirst();

            if (productEntity.isPresent()) {
//                validateOrderDetail(orderDetailRequest, productEntity.get());
                OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
                double percentage = MAX_PERCENTAGE - productEntity.get().getDiscount();
                double total = percentage * (productEntity.get().getPrice() * orderDetailRequest.getQuantity()) / MAX_PERCENTAGE;
                orderDetailsEntity.setTotal(total);
                orderDetailsEntity.setOrder(orderEntity);
                orderDetailsEntity.setProduct(productEntity.get());
                orderDetailsEntity.setQuantity(orderDetailRequest.getQuantity());
                orderDetailsEntity.setUnitPrice(productEntity.get().getPrice());
                orderDetailsEntity.setDiscount(productEntity.get().getDiscount());
                orderDetailsEntities.add(orderDetailsEntity);
                int quantityLeft = productEntity.get().getQuantity() - orderDetailRequest.getQuantity();
                productEntity.get().setQuantity(quantityLeft);
                productRepository.save(productEntity.get());
            } else {
                log.info("Not found product with id {} in the order.", orderDetailRequest.getProductId());
            }
        }

        orderEntity.setOrderDetails(orderDetailsEntities);
        orderEntity = orderRepository.save(orderEntity);

        return Converter.toModel(orderEntity, OrderResponse.class);
    }

    private List<ProductEntity> getListProductsByIds(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }


//    private void validateOrderDetail(OrderDetailRequest orderDetailRequest, ProductEntity productEntity) {
//        if (orderDetailRequest.getQuantity() > productEntity.getQuantity()) {
//            throw new CustomException(String.format("We have only %d", productEntity.getQuantity()), CommonUtils.putError("", ""));
//        }
//    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return sb.toString();
    }

    @Override
    public OrderBuilderResponse findOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        final String extraData = EMPTY;
        final String amount = String.valueOf(orderEntity.getOrderDetails().stream().mapToLong(item -> item.getTotal().longValue()).sum());
        final String notifyUrl = NOTIFY_URL;
        final String orderId = UUID.randomUUID().toString();
        final String orderInfo = String.format("Customer needs to pay %s for ordering.", amount);
        final String returnUrl = RETURN_URL;
        final String requestId = UUID.randomUUID().toString();
        final String rawSignature = String.format("partnerCode=%s&accessKey=%s&requestId=%s&amount=%s&orderId=%s&orderInfo=%s" +
                        "&returnUrl=%s&notifyUrl=%s&extraData=%s",
                this.partnerCode, this.accessKey, requestId, amount, orderId, orderInfo, returnUrl, notifyUrl, extraData);
        return OrderBuilderResponse.builder()
                .partnerCode(this.partnerCode)
                .accessKey(this.accessKey)
                .requestId(requestId)
                .orderId(orderId)
                .amount(amount)
                .orderInfo(orderInfo)
                .extraData(extraData)
                .signature(generateSignature(rawSignature))
                .redirectUrl(returnUrl)
                .ipnUrl(notifyUrl)
                .requestType("captureMoMoWallet")
                .build();
    }

    private String generateSignature(String rawData) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] rawHmac = mac.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
            return toHexString(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
            exception.printStackTrace();
        }
        return EMPTY;
    }
}
