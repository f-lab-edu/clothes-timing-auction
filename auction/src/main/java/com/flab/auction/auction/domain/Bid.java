package com.flab.auction.auction.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "bid")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
@AllArgsConstructor
public class Bid {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bidId;
	@Column(name = "user_id", nullable = false)
	@Comment("사용자 아이디")
	private String userId;
	@Column(name = "bid_price", nullable = false)
	@Comment("경매 신청 가격")
	private int bidPrice;
	@Column(name = "bid_status", nullable = false)
	@Comment("경매 상태")
	@Enumerated(EnumType.STRING)
	private BidStatus bidStatus;
	@Column(name = "bid_start_date", nullable = false)
	@Comment("경매 신청 시간")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime bidStartDate;
	@Column(name = "bid_update_date", nullable = false)
	@Comment("경매 재신청을 위한 업데이트 시간")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime bidEndDate;

}
