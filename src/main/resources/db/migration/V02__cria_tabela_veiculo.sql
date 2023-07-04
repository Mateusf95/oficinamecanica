CREATE TABLE public.veiculo
(
    codigo bigserial NOT NULL,
    nome text,
    cor text,
    placa text,
    anofabricacao text,
    marca text,
    codigo_usuario integer,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);

ALTER TABLE public.veiculo
    ADD FOREIGN KEY (codigo_usuario)
    REFERENCES public.usuario (codigo)
    NOT VALID;
