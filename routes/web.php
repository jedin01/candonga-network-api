<?php

use App\Models\CategoriaParagem;
use App\Models\CategoriaVeiculo;
use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\View;

Route::get("/", function () {
    return view("welcome");
});

Route::get("/create-vertice", function () {
    $categoriasParagem = CategoriaParagem::all();
    $categoriasVeiculo = CategoriaVeiculo::all();

    return View(
        "vertice.create",
        compact(["categoriasParagem", "categoriasVeiculo"]),
    );
});
