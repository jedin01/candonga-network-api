<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('categoria_veiculo_paragem', function (Blueprint $table) {
            $table->unsignedBigInteger('id_categoria_veiculo');
            $table->unsignedBigInteger('id_paragem'); 
            $table->foreign('id_categoria_veiculo')->references('id')->on('categoria_veiculos')->onDelete('cascade');
            $table->foreign('id_paragem')->references('id')->on('paragens')->onDelete('cascade');
            $table->timestamps(); 
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        //
    }
};
