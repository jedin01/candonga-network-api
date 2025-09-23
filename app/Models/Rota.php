<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Rota extends Model
{
    use HasFactory;

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

