CREATE TABLE departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL UNIQUE,
    department_description VARCHAR(255) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO departments (department_name, department_description)
VALUES

('Medical', 'Medical treatment and patient care'),
('Emergency', 'Emergency response and urgent care'),
('Laboratory', 'Lab tests, diagnostics and analysis'),
('Pharmacy', 'Medication, prescriptions and stock'),
('IT', 'Software, systems and technical support'),
('Security', 'Security, access control and monitoring'),
('Finance', 'Billing, accounting and financial tasks'),
('Office', 'Office work and administration support'),
('Administration', 'Management and organizational tasks'),
('Training', 'Training, education and onboarding'),
('System', 'System administration and internal system roles'),
('unassigned', 'Default department for new accounts');