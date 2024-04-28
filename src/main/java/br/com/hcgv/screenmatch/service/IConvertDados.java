package br.com.hcgv.screenmatch.service;

public interface IConvertDados {

    <T> T obterDados(String json, Class<T> classe);
}

// <T> T e usado para quando nao sabemos o tipo de retorno do metodo
// Por isso usamos o Class<T> para indicar o que queremos