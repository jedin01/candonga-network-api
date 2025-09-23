<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\CategoriaVeiculo;

class CategoriaVeiculoController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $categorias = CategoriaVeiculo::all();
        return response()->json($categorias, 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            "nome" => "required|string|max:255",
        ]);

        $categoria = CategoriaVeiculo::create($validated);

        return response()->json($categoria, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $categoria = CategoriaVeiculo::find($id);

        if (!$categoria) {
            return response()->json(
                [
                    "message" => "Categoria de veículo não encontrada",
                ],
                404,
            );
        }

        return response()->json($categoria, 200);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $categoria = CategoriaVeiculo::find($id);

        if (!$categoria) {
            return response()->json(
                [
                    "message" => "Categoria de veículo não encontrada",
                ],
                404,
            );
        }

        $validated = $request->validate([
            "nome" => "sometimes|string|max:255",
        ]);

        $categoria->update($validated);

        return response()->json($categoria, 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $categoria = CategoriaVeiculo::find($id);

        if (!$categoria) {
            return response()->json(
                [
                    "message" => "Categoria de veículo não encontrada",
                ],
                404,
            );
        }

        $categoria->delete();

        return response()->json(
            [
                "message" => "Categoria de veículo removida com sucesso",
            ],
            200,
        );
    }
}
