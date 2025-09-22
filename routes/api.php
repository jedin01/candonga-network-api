<?php

use App\Http\Controllers\Api\ParagemController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::get('/paragens', [ParagemController::class, 'index']);
Route::post('/paragens', [ParagemController::class, 'store']);
Route::get('/paragens/{id}', [ParagemController::class, 'show']);
Route::put('/paragens/{id}', [ParagemController::class, 'update']);
Route::delete('/paragens/{id}', [ParagemController::class, 'destroy']);