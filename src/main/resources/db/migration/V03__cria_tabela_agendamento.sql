CREATE TABLE public.agendamento
(
    codigo bigserial NOT NULL,
    dataAgendamento date,
    codigo_veiculo integer,
    situacao text DEFAULT 'AGENDADO',
    PRIMARY KEY (codigo)
);

ALTER TABLE public.agendamento
    ADD FOREIGN KEY (codigo_veiculo)
    REFERENCES public.veiculo (codigo)
    NOT VALID;
