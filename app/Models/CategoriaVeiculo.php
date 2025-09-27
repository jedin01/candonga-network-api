<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class CategoriaVeiculo extends Model
{
    /** @use HasFactory<\Database\Factories\CategoriaVeiculoFactory> */
    use HasFactory, SoftDeletes;

    protected $fillable = ["nome"];

    public function paragens()
    {
        return $this->hasMany(Paragem::class, "id_categoria", "id");
    }
}
