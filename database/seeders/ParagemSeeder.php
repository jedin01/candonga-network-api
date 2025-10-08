<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class ParagemSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $paragens = [
                    'Aeroporto',
                    'Asa Branca (Mercado - Bairro Talatona)',
                    'Avó Kumbi',
                    'Avó Kumbi "Comércio"',
                    'Benfica',
                    'Boavista',
                    'Bom Jesus',
                    'CAOP',
                    'Cacuaco',
                    'Cacuaco "Vila"',
                    'Cacuco "Vila"',
                    'Calemba II',
                    'Camama',
                    'Cariango "Jacaré"',
                    'Cassequel',
                    'Caxito',
                    'Congoleses',
                    'Congoleses "Praça"',
                    'Congoleses "Triângulo"',
                    'Cuca',
                    'Cusema',
                    'Estalagem',
                    'Funda',
                    'Funda "Mercado Sabado"',
                    'Golfe "Correiros"',
                    'Golfe II',
                    'Hoji Ya Henda',
                    'Kilamba',
                    'Kwanzas "Mercado"',
                    'Luanda "Sul"',
                    'Luanda "Sul" (Mercado)',
                    'Maianga',
                    'Mercado do 30',
                    'Multiperfil (Clínica)',
                    'Muri el',
                    'Mutamba',
                    "N'Goma",
                    "N'Zamba II",
                    'Nilo Formal',
                    'Palanca "Sanatório"',
                    'Panguila',
                    'Bar',
                    'Quenguela',
                    'Quicolo "Mercado"',
                    'Ramos',
                    'Rocha "Padaria"',
                    'Rotunda',
                    'Sanatório "Quimbango"',
                    'São Paulo "Praça"',
                    'São Paulo "Rádio Eclésia"',
                    'Tanque Serra',
                    'Vila de Viana',
                    'Viana "Luanda Sul"',
                    'Virona',
                    'Desvio do Zango',
                    'Zango III',
                ];
        
                foreach ($paragens as $nome) {
                    DB::table('paragens')->insert([
                        'nome' => $nome,
                    ]);
                }
    }
}
