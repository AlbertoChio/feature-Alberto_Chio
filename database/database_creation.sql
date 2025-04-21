CREATE DATABASE aplazo;

-- Enable UUID support
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Customers table
CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    second_last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL CHECK (
        date_of_birth <= CURRENT_DATE - INTERVAL '18 years' AND
        date_of_birth >= CURRENT_DATE - INTERVAL '65 years'
    ),
    gender VARCHAR(10),
    place_of_birth VARCHAR(255),
    curp VARCHAR(18),
    credit_line_amount NUMERIC(12,2) NOT NULL CHECK (credit_line_amount > 0),
    available_credit_line_amount NUMERIC(12,2) NOT NULL CHECK (available_credit_line_amount >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('ROLE_CUSTOMER','ROLE_UNKNOWN')),

    customer_id UUID UNIQUE REFERENCES customers(id) ON DELETE SET NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Loans table
CREATE TABLE loans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    status TEXT NOT NULL CHECK (status IN ('ACTIVE', 'LATE', 'COMPLETED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    commission_amount NUMERIC(12,2) NOT NULL CHECK (commission_amount > 0)
);

-- Installments table
CREATE TABLE installments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    loan_id UUID NOT NULL REFERENCES loans(id) ON DELETE CASCADE,
    amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    scheduled_payment_date DATE NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('NEXT', 'PENDING', 'ERROR'))
);

-- Audit logs table
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    action TEXT NOT NULL CHECK (action IN ('CREATE', 'UPDATE', 'READ')),
    entity_type TEXT NOT NULL,
    entity_id UUID NOT NULL,
    details TEXT, 
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);