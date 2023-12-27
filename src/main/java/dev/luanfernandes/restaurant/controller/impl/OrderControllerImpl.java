package dev.luanfernandes.restaurant.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.restaurant.controller.OrderController;
import dev.luanfernandes.restaurant.domain.request.OrderRequest;
import dev.luanfernandes.restaurant.domain.request.OrderUpdateQuantityRequest;
import dev.luanfernandes.restaurant.domain.response.OrderResponse;
import dev.luanfernandes.restaurant.facade.OrderFacade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderFacade orderFacade;

    @Override
    public ResponseEntity<OrderResponse> create(OrderRequest request) {
        return status(CREATED).body(orderFacade.create(request));
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ok(orderFacade.getAll());
    }

    @Override
    public ResponseEntity<OrderResponse> getById(Long id) {
        return ok(orderFacade.getById(id));
    }

    @Override
    public ResponseEntity<Object> updateProductQuantityInOrder(Long id, OrderUpdateQuantityRequest request) {
        orderFacade.updateProductQuantityInOrder(id, request);
        return noContent().build();
    }
}
