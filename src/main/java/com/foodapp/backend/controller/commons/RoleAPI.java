package com.foodapp.backend.controller.commons;



import com.foodapp.backend.dto.RoleDTO;
import com.foodapp.backend.dto.request.RoleRequest;
import com.foodapp.backend.dto.response.RoleResponse;
import com.foodapp.backend.service.RoleService;
import com.foodapp.backend.utils.ResponseEntityBuilder;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(
        tags = "Role-Resource",
        description = "Providing api for Role"
)
@RestController
@RequestMapping("/api")
public class RoleAPI {

    private final RoleService roleService;

    @Autowired
    public RoleAPI(RoleService roleService) {
        this.roleService = roleService;
    }


    @RequestMapping(value = "/admin/role/create-role", method = RequestMethod.POST)
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        RoleResponse response = roleService.create(roleRequest);
        return ResponseEntityBuilder.getBuilder()
                .setMessage("Create a role successfully").setDetails(response)
                .build();
    }

    /**
     * @param roleRequest
     * @return
     */
    @RequestMapping(value = "/admin/role/update-role", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRole(@RequestBody RoleDTO roleRequest, @PathVariable("id") long id) {
        roleRequest.setId(id);
        RoleResponse response = roleService.update(roleRequest);
        return ResponseEntityBuilder.getBuilder()
                .setDetails(response)
                .setMessage("Update a lesson successfully")
                .build();
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/admin/role/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getListRole() {
        List<RoleResponse> responseList = roleService.getListRole();
        return ResponseEntityBuilder.getBuilder().setDetails(responseList).setMessage("Get list role successfully").build();
    }
}
