export interface LoanInfo {
  id: string;
  customerId: string;
  createdAt: Date;
  status: 'ACTIVE' | 'LATE' | 'COMPLETE';
  paymentPlan: {
    installments: Array<{
      amount: number;
      scheduledPaymentDate: Date;
      status: 'NEXT' | 'PENDING' | 'ERROR';
    }>;
    commissionAmount: number;
  };
}
