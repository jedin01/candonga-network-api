<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Bairro extends Model
{
    use HasFactory;

    protected $table = 'bairros';

    protected $fillable = ['nome'];

    public function paragens()
    {
        return $this->hasMany(Paragem::class, 'id_bairro');
    }
}
