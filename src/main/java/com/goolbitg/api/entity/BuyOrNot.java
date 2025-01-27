package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * BuyOrNot
 */
@Entity
@Table(name = "buyornots")
@Getter
@Setter
public class BuyOrNot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "writer_id")
    private String writerId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "product_image_url")
    private String productImageUrl;

    @Column(name = "good_reason")
    private String goodReason;

    @Column(name = "bad_reason")
    private String badReason;

}
