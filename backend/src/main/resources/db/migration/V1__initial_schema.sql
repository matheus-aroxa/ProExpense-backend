CREATE TABLE users (
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE account (
    id       UUID         NOT NULL,
    name     VARCHAR(255) NOT NULL,
    owner_id UUID         NOT NULL,
    CONSTRAINT account_pkey PRIMARY KEY (id),
    CONSTRAINT fk_account_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE account_members (
    id         UUID    NOT NULL,
    is_active  BOOLEAN NOT NULL DEFAULT TRUE,
    account_id UUID    NOT NULL,
    user_id    UUID    NOT NULL,
    CONSTRAINT account_members_pkey PRIMARY KEY (id),
    CONSTRAINT uk_account_members_account_user UNIQUE (account_id, user_id),
    CONSTRAINT fk_account_members_account FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT fk_account_members_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE card (
    id             UUID           NOT NULL,
    is_active      BOOLEAN        NOT NULL DEFAULT TRUE,
    card_limit     NUMERIC(12, 2),
    name           VARCHAR(255)   NOT NULL,
    payment_method VARCHAR(255)   NOT NULL,
    account_id     UUID           NOT NULL,
    owner_id       UUID           NOT NULL,
    CONSTRAINT card_pkey PRIMARY KEY (id),
    CONSTRAINT card_payment_method_check CHECK (payment_method IN ('CREDIT', 'DEBIT', 'BOTH')),
    CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT fk_card_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE category (
    id         UUID         NOT NULL,
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    name       VARCHAR(255) NOT NULL,
    account_id UUID         NOT NULL,
    CONSTRAINT category_pkey PRIMARY KEY (id),
    CONSTRAINT uk_category_account_name UNIQUE (account_id, name),
    CONSTRAINT fk_category_account FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE expense (
    id                  UUID           NOT NULL,
    amount              NUMERIC(12, 2) NOT NULL,
    current_installment INTEGER        NOT NULL DEFAULT 0,
    description         VARCHAR(255),
    expense_date        DATE           NOT NULL,
    installments_number INTEGER        NOT NULL,
    is_in_installments  BOOLEAN        NOT NULL,
    name                VARCHAR(255)   NOT NULL,
    payment_method      SMALLINT       NOT NULL,
    account_id          UUID           NOT NULL,
    card_id             UUID           NOT NULL,
    category_id         UUID,
    owner_id            UUID           NOT NULL,
    CONSTRAINT expense_pkey PRIMARY KEY (id),
    CONSTRAINT expense_payment_method_check CHECK (payment_method >= 0 AND payment_method <= 1),
    CONSTRAINT fk_expense_account FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT fk_expense_card FOREIGN KEY (card_id) REFERENCES card (id),
    CONSTRAINT fk_expense_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_expense_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

ALTER TABLE users           ENABLE ROW LEVEL SECURITY;
ALTER TABLE account         ENABLE ROW LEVEL SECURITY;
ALTER TABLE account_members ENABLE ROW LEVEL SECURITY;
ALTER TABLE card            ENABLE ROW LEVEL SECURITY;
ALTER TABLE category        ENABLE ROW LEVEL SECURITY;
ALTER TABLE expense         ENABLE ROW LEVEL SECURITY;
