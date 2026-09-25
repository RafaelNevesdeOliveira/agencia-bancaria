-- Relações completas entre conta, titular e tipo.
SELECT
    c.id,
    c.agencia,
    c.numero,
    c.saldo,
    c.ativa,
    p.id AS titular_id,
    p.nome AS titular,
    p.cpf,
    t.id AS tipo_conta_id,
    t.nome AS tipo_conta
FROM public.contas_bancarias c
JOIN public.pessoas p ON p.id = c.titular_id
JOIN public.tipos_conta t ON t.id = c.tipo_conta_id
ORDER BY p.nome, c.numero;

-- Quantidade de contas e saldo total por tipo.
SELECT
    t.nome AS tipo_conta,
    COUNT(c.id) AS total_contas,
    COALESCE(SUM(c.saldo), 0) AS saldo_total
FROM public.tipos_conta t
LEFT JOIN public.contas_bancarias c ON c.tipo_conta_id = t.id
GROUP BY t.id, t.nome
ORDER BY t.nome;

-- Quantidade de contas e saldo total por titular.
SELECT
    p.id AS titular_id,
    p.nome AS titular,
    COUNT(c.id) AS total_contas,
    COALESCE(SUM(c.saldo), 0) AS saldo_total
FROM public.pessoas p
LEFT JOIN public.contas_bancarias c ON c.titular_id = p.id
GROUP BY p.id, p.nome
ORDER BY p.nome;

-- Consulta simples para conferir saldo antes e depois de depósito ou saque.
SELECT id, agencia, numero, saldo, ativa
FROM public.contas_bancarias
WHERE id = 1;




SELECT
    -- Nome do tipo de conta, como CORRENTE, POUPANCA ou SALARIO.
    t.nome AS tipo_conta,

    -- Conta quantas contas bancárias estão relacionadas ao tipo.
    -- COUNT(c.id) ignora valores nulos.
    -- Por isso, um tipo sem contas retorna zero.
    COUNT(c.id) AS total_contas,

    -- Soma o saldo de todas as contas relacionadas ao tipo.
    -- Se o tipo não possuir contas, SUM retornará NULL.
    -- COALESCE substitui esse NULL pelo valor zero.
    COALESCE(SUM(c.saldo), 0) AS saldo_total

-- A consulta começa pela tabela de tipos de conta.
FROM public.tipos_conta AS t

         -- LEFT JOIN mantém todos os tipos de conta no resultado,
-- mesmo quando ainda não existe uma conta associada.
         LEFT JOIN public.contas_bancarias AS c
    -- Relacionamento entre a chave estrangeira da conta
    -- e a chave primária do tipo de conta.
                   ON c.tipo_conta_id = t.id

-- Agrupa as contas por tipo.
-- O ID é incluído porque identifica cada tipo de forma única.
GROUP BY
    t.id,
    t.nome

-- Organiza o resultado alfabeticamente pelo nome do tipo.
ORDER BY
    t.nome;


-- Insere uma nova pessoa.
INSERT INTO public.pessoas (
    nome,
    cpf,
    email
)
VALUES (
           'Rafael Oliveira',
           '45678912300',
           'rafael.oliveira@example.com'
       );

-- Consulta as pessoas cadastradas.
SELECT *
FROM public.pessoas
ORDER BY id;

UPDATE public.pessoas
            SET
                nome = 'Rafael Neves de Oliveira',
                email = 'rafael.neves@example.com'
            WHERE id = 1;

-- Confere os dados atualizados.
SELECT *
FROM public.pessoas
WHERE id = 1;