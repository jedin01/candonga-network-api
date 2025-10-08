<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class CategoriaParagemSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $categorias = [
            [
                "nome" => "Terminal Principal",
                "descricao" =>
                    "Ponto de partida e chegada que inicia e encerra as operações da rede de transportes, sendo um hub central para o fluxo de passageiros e veículos.",
            ],
            [
                "nome" => "Terminal Intermediário",
                "descricao" =>
                    "Ponto de conexão ou transbordo na rede, onde os passageiros mudam de veículo ou rota para alcançar o destino final.",
            ],
            [
                "nome" => "Apeadeiro Secundário",
                "descricao" =>
                    "Paragem obrigatória ao longo das rotas, servindo como ponto de apoio ou conveniência, mas sem função de terminal.",
            ],
        ];

        DB::table("categoria_paragems")->insert($categorias);
    }
}
