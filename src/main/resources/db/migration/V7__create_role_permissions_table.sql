-- Table pour les permissions par rôle
CREATE TABLE role_permissions (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    permission_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(role_name, permission_name)
);

CREATE INDEX idx_role_permissions_role_name ON role_permissions(role_name);

INSERT INTO role_permissions (role_name, permission_name, description) VALUES
('standard', 'VIEW_SCPI_LIST', 'Accès à la liste des SCPI'),
('standard', 'VIEW_PORTFOLIO', 'Accès au portefeuille'),
('standard', 'VIEW_SCHEDULED_PAYMENTS', 'Accès aux versements programmés'),
('standard', 'VIEW_SIMULATIONS', 'Accès aux simulations'),
('standard', 'VIEW_DOCUMENTS', 'Accès aux documents réglementaires'),
('standard', 'VIEW_REQUEST_HISTORY', 'Accès à l''historique des demandes');

INSERT INTO role_permissions (role_name, permission_name, description) VALUES
('premium', 'VIEW_SCPI_LIST', 'Accès à la liste des SCPI'),
('premium', 'VIEW_PORTFOLIO', 'Accès au portefeuille'),
('premium', 'VIEW_SCHEDULED_PAYMENTS', 'Accès aux versements programmés'),
('premium', 'VIEW_SIMULATIONS', 'Accès aux simulations'),
('premium', 'VIEW_DOCUMENTS', 'Accès aux documents réglementaires'),
('premium', 'VIEW_REQUEST_HISTORY', 'Accès à l''historique des demandes'),
('premium', 'VIEW_COMPARATOR', 'Accès au comparateur SCPI');

