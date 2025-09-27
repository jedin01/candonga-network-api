<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Paragem extends Model
{
    /** @use HasFactory<\Database\Factories\ParagemFactory> */
    use HasFactory, SoftDeletes;

    use SoftDeletes;

    protected $fillable = ['nome', 'latitude', 'longitude', 'id_categoria'];
    protected $table = 'paragens';


    public function categoria()
    {
        return $this->belongsTo(CategoriaVeiculo::class, "id_categoria");
    }

}
