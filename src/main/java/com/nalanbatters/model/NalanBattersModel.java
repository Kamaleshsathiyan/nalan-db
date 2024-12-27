package com.nalanbatters.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NalanBattersModel {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
	@Column(unique = true)
    private String orderId; 
	private String username;
	@Column(nullable = false, length = 50)
    private String email;
    private Long phoneNumber;
    private String address;
    private String dayAndPlace;
    private int twoLtrQty;
    private int twoLtrPrice;
    private int twoLtrTotal;
    private int fiveLtrQty;
    private int fiveLtrPrice;
    private int fiveLtrTotal;
    private float amount;
    private String status;
    
}
