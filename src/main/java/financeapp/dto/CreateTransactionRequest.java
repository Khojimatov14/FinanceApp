package financeapp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {
    private Long userId;
    private BigDecimal amount;
    private String description;
}