package com.example.aplazo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.aplazo.dto.CustomerResponseDTO;
import com.example.aplazo.dto.LoanResponseDTO;
import com.example.aplazo.entity.AuditLog;
import com.example.aplazo.entity.Customer;
import com.example.aplazo.entity.Installment;
import com.example.aplazo.entity.Loan;
import com.example.aplazo.entity.User;
import com.example.aplazo.exception.CustomerNotFoundException;
import com.example.aplazo.exception.LoanNotFoundException;
import com.example.aplazo.mapper.LoanMapper;
import com.example.aplazo.repository.AuditLogRepository;
import com.example.aplazo.repository.CustomerRepository;
import com.example.aplazo.repository.InstallmentRepository;
import com.example.aplazo.repository.LoanRepository;

import jakarta.transaction.Transactional;

@Service
public class LoanService {

	private final LoanRepository loanRepository;
	private final CustomerRepository customerRepository;
	private final InstallmentRepository installmentRepository;
	private final AuditLogRepository auditLogRepository;

	public LoanService(LoanRepository loanRepository, CustomerRepository customerRepository,
			InstallmentRepository installmentRepository, AuditLogRepository auditLogRepository) {
		this.loanRepository = loanRepository;
		this.customerRepository = customerRepository;
		this.installmentRepository = installmentRepository;
		this.auditLogRepository = auditLogRepository;
	}

	@Transactional
	public LoanResponseDTO createLoan(UUID customerId, Double amount) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
		validateSufficientCredit(customer, amount);

		BigDecimal amountBD = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
		BigDecimal totalBD = calculateTotalWithCommission(amountBD);
		double commissionAmount = totalBD.subtract(amountBD).doubleValue();

		Loan loan = createAndSaveLoan(customerId, totalBD.doubleValue(), commissionAmount);
		logCreation("Loan", "CREATE", loan.getId(), customerId, loan.toString());

		List<Installment> installments = createInstallments(loan.getId(), totalBD);
		List<Installment> savedInstallments = installmentRepository.saveAll(installments);
		logInstallments(savedInstallments, customerId);

		updateCustomerCredit(customer, totalBD.doubleValue());

		return LoanMapper.toDTO(loan, savedInstallments);
	}

	public LoanResponseDTO getLoan(UUID id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();
		UUID userId = userDetails.getId();
		Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException("Loan not found"));
		logCreation("Loan", "READ", id, userId, null);
		return LoanMapper.toDTO(loan, loan.getInstallments());
	}

	private void validateSufficientCredit(Customer customer, double amount) {
		double totalAmount = amount * 1.3;
		if (customer.getAvailableCreditLineAmount() < totalAmount) {
			throw new IllegalArgumentException("Insufficient available credit");
		}
	}

	private BigDecimal calculateTotalWithCommission(BigDecimal baseAmount) {
		BigDecimal commissionRate = BigDecimal.valueOf(0.3);
		return baseAmount.multiply(BigDecimal.ONE.add(commissionRate)).setScale(2, RoundingMode.HALF_UP);
	}

	private Loan createAndSaveLoan(UUID customerId, double totalAmount, double commissionAmount) {
		Loan loan = new Loan();
		loan.setCustomerId(customerId);
		loan.setAmount(totalAmount);
		loan.setCommissionAmount(commissionAmount);
		loan.setStatus("ACTIVE");
		return loanRepository.save(loan);
	}

	private List<Installment> createInstallments(UUID loanId, BigDecimal totalAmount) {
		List<Installment> installments = new ArrayList<>();
		BigDecimal baseAmount = totalAmount.divide(BigDecimal.valueOf(5), 2, RoundingMode.UP);

		for (int i = 1; i <= 5; i++) {
			Installment installment = new Installment();
			installment.setLoanId(loanId);
			installment.setAmount(baseAmount.doubleValue());
			installment.setScheduledPaymentDate(LocalDate.now().plusMonths(i));
			installment.setStatus("NEXT");
			installments.add(installment);
		}

		return installments;
	}

	private void logCreation(String entityType, String action, UUID entityId, UUID userId, String details) {
		AuditLog log = new AuditLog();
		log.setUserId(userId);
		log.setAction(action);
		log.setEntityType(entityType);
		log.setEntityId(entityId);
		log.setDetails(details);
		auditLogRepository.save(log);
	}

	private void logInstallments(List<Installment> installments, UUID userId) {
		List<AuditLog> logs = installments.stream().map(installment -> {
			AuditLog log = new AuditLog();
			log.setUserId(userId);
			log.setAction("CREATE");
			log.setEntityType("Installment");
			log.setEntityId(installment.getId());
			log.setDetails(installment.toString());
			return log;
		}).toList();

		auditLogRepository.saveAll(logs);
	}

	private void updateCustomerCredit(Customer customer, double totalAmount) {
		double newAvailableCredit = customer.getAvailableCreditLineAmount() - totalAmount;
		customer.setAvailableCreditLineAmount(newAvailableCredit);
		Customer updatedCustomer = customerRepository.save(customer);
		logCreation("Customer", "UPDATE", updatedCustomer.getId(), updatedCustomer.getId(), updatedCustomer.toString());
	}

}