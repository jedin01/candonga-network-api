<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CategoriaVeiculo extends Model
{
    /** @use HasFactory<\Database\Factories\CategoriaVeiculoFactory> */
    use HasFactory;

    protected $fillable = ["nome"];

    public function paragens()
    {
        return $this->hasMany(Paragem::class, "id_categoria", "id");
    }
}
