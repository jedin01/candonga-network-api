<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CategoriaParagem extends Model
{
    /** @use HasFactory<\Database\Factories\CategoriaParagemFactory> */
    use HasFactory;
    
    protected $fillable = ["nome", "descricao"];
}
