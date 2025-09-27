<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class CategoriaParagem extends Model
{
    /** @use HasFactory<\Database\Factories\CategoriaParagemFactory> */
    use HasFactory, SoftDeletes;
    
    protected $fillable = ["nome", "descricao"];
}
