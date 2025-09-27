<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Rota extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = ['origem_id', 'destino_id', 'distancia', 'custo'];

    
    public function origem()
    {
        return $this->belongsTo(Paragem::class, 'origem_id');
    }

    public function destino()
    {
        return $this->belongsTo(Paragem::class, 'destino_id');
    }
}

