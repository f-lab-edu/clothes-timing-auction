package com.flab.auction.auction.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "auction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
@AllArgsConstructor
public class Auction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer auctionId;
	private Integer productId;
	@Column(name = "auction_start_date", nullable = false)
	@Comment("경매 시작 시간")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime auctionStartDate;

	@Column(name = "auction_end_date", nullable = false)
	@Comment("경매 종료 시간")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime auctionEndDate;
	@Min(value = 1, message = "경매 시작가")
	private Integer auctionStartPrice;

	@Column(name = "auction_regist_date", nullable = false)
	@Comment("경매 종료 시간")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime auctionRegistDate;
	@Column(name = "quantity", nullable = false)
	@Comment("경매 수량")
	private int quantity;

}
