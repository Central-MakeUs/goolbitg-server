package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BuyOrNot
 */
@Entity
@Table(name = "buyornots")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyOrNot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "writer_id")
    private String writerId;

    @Setter
    @Column(name = "product_name")
    private String productName;

    @Setter
    @Column(name = "product_price")
    private Integer productPrice;

    @Setter
    @Column(name = "product_image_url")
    private String productImageUrl;

    @Setter
    @Column(name = "good_reason")
    private String goodReason;

    @Setter
    @Column(name = "bad_reason")
    private String badReason;

}
