<?php

use App\Http\Controllers\Api\ParagemController;
use App\Http\Controllers\Api\RotaController;
use App\Http\Controllers\Api\CategoriaParagemController;
use App\Http\Controllers\Api\CategoriaVeiculoController;
use App\Http\Controllers\Api\BairroController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::get("/user", function (Request $request) {
    return $request->user();
})->middleware("auth:sanctum");

// Rotas para Paragens
Route::get("/paragens", [ParagemController::class, "index"]);
Route::post("/paragens", [ParagemController::class, "store"]);
Route::get("/paragens/{id}", [ParagemController::class, "show"]);
Route::put("/paragens/{id}", [ParagemController::class, "update"]);
Route::delete("/paragens/{id}", [ParagemController::class, "destroy"]);

// Rotas para Rotas
Route::get("/rotas", [RotaController::class, "index"]);
Route::post("/rotas", [RotaController::class, "store"]);
Route::get("/rotas/{id}", [RotaController::class, "show"]);
Route::put("/rotas/{id}", [RotaController::class, "update"]);
Route::delete("/rotas/{id}", [RotaController::class, "destroy"]);

// Rotas para Categoria de Paragem
Route::get("/categoria-paragens", [CategoriaParagemController::class, "index"]);
Route::post("/categoria-paragens", [
    CategoriaParagemController::class,
    "store",
]);
Route::get("/categoria-paragens/{id}", [
    CategoriaParagemController::class,
    "show",
]);
Route::put("/categoria-paragens/{id}", [
    CategoriaParagemController::class,
    "update",
]);
Route::delete("/categoria-paragens/{id}", [
    CategoriaParagemController::class,
    "destroy",
]);

// Rotas para Categoria de Veículo
Route::get("/categoria-veiculos", [CategoriaVeiculoController::class, "index"]);
Route::post("/categoria-veiculos", [
    CategoriaVeiculoController::class,
    "store",
]);
Route::get("/categoria-veiculos/{id}", [
    CategoriaVeiculoController::class,
    "show",
]);
Route::put("/categoria-veiculos/{id}", [
    CategoriaVeiculoController::class,
    "update",
]);
Route::delete("/categoria-veiculos/{id}", [
    CategoriaVeiculoController::class,
    "destroy",
]);

// Rotas para Bairros
Route::get("/bairros", [BairroController::class, "index"]);
Route::post("/bairros", [BairroController::class, "store"]);
Route::get("/bairros/{id}", [BairroController::class, "show"]);
Route::put("/bairros/{id}", [BairroController::class, "update"]);
Route::delete("/bairros/{id}", [BairroController::class, "destroy"]);
