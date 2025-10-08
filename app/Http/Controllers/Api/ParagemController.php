<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Paragem;

class ParagemController extends Controller
{
    public function index()
    {
        $paragens = Paragem::all();
        return response()->json($paragens, 200);
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            "nome" => "required|string|max:255",
            "latitude" => "required|numeric",
            "longitude" => "required|numeric",
            "id_categoria" => "required|integer",
        ]);

        $paragem = Paragem::create($validated);

        return response()->json($paragem, 201);
    }

    public function show(string $id)
    {
        $paragem = Paragem::find($id);

        if (!$paragem) {
            return response()->json(
                ["message" => "Paragem não encontrada"],
                404,
            );
        }

        return response()->json($paragem, 200);
    }

    public function update(Request $request, string $id)
    {
        $paragem = Paragem::find($id);

        if (!$paragem) {
            return response()->json(
                ["message" => "Paragem não encontrada"],
                404,
            );
        }

        $validated = $request->validate([
            "nome" => "sometimes|string|max:255",
            "latitude" => "sometimes|numeric",
            "longitude" => "sometimes|numeric",
            "id_categoria" => "sometimes|integer",
        ]);

        $paragem->update($validated);

        return response()->json($paragem, 200);
    }

    public function destroy(string $id)
    {
        $paragem = Paragem::find($id);

        if (!$paragem) {
            return response()->json(
                ["message" => "Paragem não encontrada"],
                404,
            );
        }

        $paragem->delete();

        return response()->json(
            ["message" => "Paragem removida com sucesso"],
            200,
        );
    }
}p
