-- Execute uma única vez conectado ao database postgres, com autocommit ativo.
-- No pgAdmin ou DBeaver, execute somente este comando antes de trocar a conexão.
-- Se o database já existir, não execute novamente.

CREATE DATABASE desafio_bancario_correcao
    WITH ENCODING = 'UTF8';

