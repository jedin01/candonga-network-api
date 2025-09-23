<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Rota;

class RotaController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $rotas = Rota::with(["origem", "destino"])->get();
        return response()->json($rotas, 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            "origem_id" => "required|integer|exists:paragens,id",
            "destino_id" =>
                "required|integer|exists:paragens,id|different:origem_id",
            "distancia" => "required|numeric|min:0",
            "custo" => "required|numeric|min:0",
        ]);

        $rota = Rota::create($validated);

        return response()->json($rota->load(["origem", "destino"]), 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $rota = Rota::with(["origem", "destino"])->find($id);

        if (!$rota) {
            return response()->json(
                [
                    "message" => "Rota não encontrada",
                ],
                404,
            );
        }

        return response()->json($rota, 200);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $rota = Rota::find($id);

        if (!$rota) {
            return response()->json(
                [
                    "message" => "Rota não encontrada",
                ],
                404,
            );
        }

        $validated = $request->validate([
            "origem_id" => "sometimes|integer|exists:paragens,id",
            "destino_id" =>
                "sometimes|integer|exists:paragens,id|different:origem_id",
            "distancia" => "sometimes|numeric|min:0",
            "custo" => "sometimes|numeric|min:0",
        ]);

        $rota->update($validated);

        return response()->json($rota->load(["origem", "destino"]), 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $rota = Rota::find($id);

        if (!$rota) {
            return response()->json(
                [
                    "message" => "Rota não encontrada",
                ],
                404,
            );
        }

        $rota->delete();

        return response()->json(
            [
                "message" => "Rota removida com sucesso",
            ],
            200,
        );
    }
}
