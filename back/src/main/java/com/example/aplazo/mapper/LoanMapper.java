package com.example.aplazo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.aplazo.dto.LoanResponseDTO;
import com.example.aplazo.dto.LoanResponseDTO.InstallmentDTO;
import com.example.aplazo.dto.LoanResponseDTO.PaymentPlanDTO;
import com.example.aplazo.entity.Installment;
import com.example.aplazo.entity.Loan;

public class LoanMapper {

	public static LoanResponseDTO toDTO(Loan loan, List<Installment> installments) {
		List<InstallmentDTO> installmentDTOs = installments.stream().map(inst -> {
			InstallmentDTO dto = new InstallmentDTO();
			dto.setAmount(inst.getAmount());
			dto.setScheduledPaymentDate(inst.getScheduledPaymentDate());
			dto.setStatus(inst.getStatus());
			return dto;
		}).collect(Collectors.toList());

		PaymentPlanDTO paymentPlan = new PaymentPlanDTO();
		paymentPlan.setInstallments(installmentDTOs);
		paymentPlan.setCommissionAmount(loan.getCommissionAmount());

		LoanResponseDTO response = new LoanResponseDTO();
		response.setId(loan.getId());
		response.setCustomerId(loan.getCustomerId());
		response.setCreatedAt(loan.getCreatedAt());
		response.setStatus(loan.getStatus());
		response.setPaymentPlan(paymentPlan);
		return response;
	}
}