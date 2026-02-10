package com.thirupathi.gym.Dto;

import com.thirupathi.gym.Entity.Enum.PaymentStatus;
import com.thirupathi.gym.Entity.Enum.Plans;
import com.thirupathi.gym.Entity.Enum.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class GymDto {

    private String userName;
    @Enumerated(EnumType.STRING)
    private Plans plan;
    private Long weight;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus feesStatus;

    public GymDto(){}

    public GymDto(String userName, Plans plan, Long weight, Status status, PaymentStatus feesStatus) {
        this.userName = userName;
        this.plan = plan;
        this.weight = weight;
        this.status = status;
        this.feesStatus = feesStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Plans getPlan() {
        return plan;
    }

    public void setPlan(Plans plan) {
        this.plan = plan;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PaymentStatus getFeesStatus() {
        return feesStatus;
    }

    public void setFeesStatus(PaymentStatus feesStatus) {
        this.feesStatus = feesStatus;
    }
}
