<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Bairro;
use Illuminate\Http\Request;

class BairroController extends Controller
{
    public function index()
    {
        return response()->json(Bairro::all());
    }

    /**
     * Cria um novo bairro
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            "nome" => "required|string|max:255",
        ]);

        $bairro = Bairro::create($validated);

        return response()->json($bairro, 201);
    }

    /**
     * Mostra um bairro específico
     */
    public function show(string $id)
    {
        $bairro = Bairro::findOrFail($id);
        return response()->json($bairro);
    }

    /**
     * Atualiza um bairro
     */
    public function update(Request $request, string $id)
    {
        $bairro = Bairro::findOrFail($id);

        $validated = $request->validate([
            "nome" => "required|string|max:255",
        ]);

        $bairro->update($validated);

        return response()->json($bairro);
    }

    /**
     * Remove um bairro
     */
    public function destroy(string $id)
    {
        $bairro = Bairro::findOrFail($id);
        $bairro->delete();

        return response()->json(null, 204);
    }
}
