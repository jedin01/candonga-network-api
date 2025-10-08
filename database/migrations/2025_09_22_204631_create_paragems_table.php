<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('paragens', function (Blueprint $table) {
            $table->id();
                $table->string('nome');
                $table->decimal('latitude', 10, 7)->nullable();
                $table->decimal('longitude', 10, 7)->nullable();
                $table->unsignedBigInteger('id_categoria')->nullable();
                $table->timestamps();
                $table->softDeletes();
                $table->foreign('id_categoria')->references('id')->on('categoria_veiculos')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('paragems');
    }
};
