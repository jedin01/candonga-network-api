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
        Schema::create("ligacoes", function (Blueprint $table) {
            $table->id();

            $table
                ->foreignId("origem_id")
                ->constrained("paragens")
                ->onDelete("cascade");

            $table
                ->foreignId("destino_id")
                ->constrained("paragens")
                ->onDelete("cascade");

            $table->float("distancia")->nullable();
            $table->decimal("custo", 8, 2)->nullable(); 

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists("ligacaos");
    }
};
