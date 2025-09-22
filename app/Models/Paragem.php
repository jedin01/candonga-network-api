<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Paragem extends Model
{
    /** @use HasFactory<\Database\Factories\ParagemFactory> */
    use HasFactory;
    
    use SoftDeletes;
    
    protected $fillable = ['nome', 'latitude', 'longitude', 'id_bairro'];
    protected $table = 'paragens';
    
}
