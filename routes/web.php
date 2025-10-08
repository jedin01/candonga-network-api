<?php

use App\Models\CategoriaParagem;
use App\Models\CategoriaVeiculo;
use App\Models\Paragem;
use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\View;
use PhpParser\Node\Param;

Route::get("/", function () {
    return view("welcome");
});

Route::get("/create-vertice", function () {
    $categoriasParagem = CategoriaParagem::all();
    $categoriasVeiculo = CategoriaVeiculo::all();
    $paragens = Paragem::all();
    return View(
        "vertice.create",
        compact(["categoriasParagem", "categoriasVeiculo", "paragens"]),
    );
});
