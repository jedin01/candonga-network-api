<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Bairro extends Model
{
    use HasFactory, SoftDeletes;

    protected $table = 'bairros';

    protected $fillable = ['nome'];

    public function paragens()
    {
        return $this->hasMany(Paragem::class, 'id_bairro');
    }
}
