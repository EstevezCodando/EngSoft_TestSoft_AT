package br.com.infnet.at.atviacepex3;

import br.com.infnet.at.atviacepex3.client.ViaCepClient;
import br.com.infnet.at.atviacepex3.model.ViaCepEndereco;

import java.util.Optional;

public class AtViacepEx3Application {

    public static void main(String[] args) {
        ViaCepClient client = new ViaCepClient();

        String cep = "01001000";
        Optional<ViaCepEndereco> endereco = client.buscarPorCep(cep);

        if (endereco.isPresent()) {
            ViaCepEndereco e = endereco.get();
            System.out.println("Consulta ViaCEP bem-sucedida:");
            System.out.println("CEP: " + e.getCep());
            System.out.println("Logradouro: " + e.getLogradouro());
            System.out.println("Bairro: " + e.getBairro());
            System.out.println("Cidade: " + e.getLocalidade());
            System.out.println("UF: " + e.getUf());
        } else {
            System.out.println("CEP não encontrado: " + cep);
        }
    }
}
