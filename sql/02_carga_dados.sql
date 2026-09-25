-- Execute depois de 01_criar_tabelas.sql.
-- A carga é repetível porque trata conflitos pelas chaves únicas.

INSERT INTO public.tipos_conta (nome)
VALUES
    ('CORRENTE'),
    ('POUPANCA'),
    ('SALARIO')
ON CONFLICT (nome) DO NOTHING;

INSERT INTO public.pessoas (nome, cpf, email)
VALUES
    ('Mariana Costa', '12345678901', 'mariana.costa@example.com'),
    ('Joao Martins', '98765432100', 'joao.martins@example.com'),
    ('Aline Souza', '11122233344', 'aline.souza@example.com'),
    ('Camila Ferreira', '55566677788', 'camila.ferreira@example.com')
ON CONFLICT (cpf) DO UPDATE
SET nome = EXCLUDED.nome,
    email = EXCLUDED.email;

INSERT INTO public.contas_bancarias
    (agencia, numero, saldo, ativa, titular_id, tipo_conta_id)
SELECT '0001', '100001-0', 1500.00, TRUE, p.id, t.id
FROM public.pessoas p
CROSS JOIN public.tipos_conta t
WHERE p.cpf = '12345678901'
  AND t.nome = 'CORRENTE'
ON CONFLICT (agencia, numero) DO UPDATE
SET saldo = EXCLUDED.saldo,
    ativa = EXCLUDED.ativa,
    titular_id = EXCLUDED.titular_id,
    tipo_conta_id = EXCLUDED.tipo_conta_id;

INSERT INTO public.contas_bancarias
    (agencia, numero, saldo, ativa, titular_id, tipo_conta_id)
SELECT '0001', '100002-8', 4200.50, TRUE, p.id, t.id
FROM public.pessoas p
CROSS JOIN public.tipos_conta t
WHERE p.cpf = '98765432100'
  AND t.nome = 'POUPANCA'
ON CONFLICT (agencia, numero) DO UPDATE
SET saldo = EXCLUDED.saldo,
    ativa = EXCLUDED.ativa,
    titular_id = EXCLUDED.titular_id,
    tipo_conta_id = EXCLUDED.tipo_conta_id;

INSERT INTO public.contas_bancarias
    (agencia, numero, saldo, ativa, titular_id, tipo_conta_id)
SELECT '0001', '100003-6', 0.00, FALSE, p.id, t.id
FROM public.pessoas p
CROSS JOIN public.tipos_conta t
WHERE p.cpf = '11122233344'
  AND t.nome = 'SALARIO'
ON CONFLICT (agencia, numero) DO UPDATE
SET saldo = EXCLUDED.saldo,
    ativa = EXCLUDED.ativa,
    titular_id = EXCLUDED.titular_id,
    tipo_conta_id = EXCLUDED.tipo_conta_id;

SELECT
    c.id AS conta_id,
    c.agencia,
    c.numero,
    c.saldo,
    c.ativa,
    p.id AS titular_id,
    p.nome AS titular,
    t.id AS tipo_conta_id,
    t.nome AS tipo_conta
FROM public.contas_bancarias c
JOIN public.pessoas p ON p.id = c.titular_id
JOIN public.tipos_conta t ON t.id = c.tipo_conta_id
ORDER BY c.id;

