package com.foodapp.backend.controller.orders;



import com.foodapp.backend.dto.OrderDTO;
import com.foodapp.backend.dto.response.BaseRestResponse;
import com.foodapp.backend.dto.response.OrderBuilderResponse;
import com.foodapp.backend.dto.response.OrderResponse;
import com.foodapp.backend.exceptions.AppException;
import com.foodapp.backend.service.OrderService;
import com.foodapp.backend.utils.ResponseEntityBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Order-Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Order-API-Resource", description = "Write description here")
})
@RestController
@RequestMapping(value = "/api")
public class OrderAPI {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/admin/order/create-order", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody OrderDTO orderDto) {
        OrderResponse orderDTO = orderService.createOrder(orderDto);
        return ResponseEntityBuilder.getBuilder()
                .setDetails(orderDTO)
                .setMessage("Create a order successfully")
                .build();
    }

    @RequestMapping(value = "/order/get-order", method = RequestMethod.GET)
    public ResponseEntity<?> getOrder(@RequestParam Long orderId) {
        try {
            OrderBuilderResponse response = orderService.findOrderById(orderId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppException exception) {
            BaseRestResponse response = BaseRestResponse.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .success(false)
                    .message(exception.getMessage())
                    .buildResponse();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
