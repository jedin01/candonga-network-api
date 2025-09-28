<!DOCTYPE html>
<html lang="pt">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cadastrar Paragem</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap" rel="stylesheet">
  <style>
    body {
      font-family: 'Inter', sans-serif;
    }
  </style>
</head>
<body class="bg-gray-50 flex items-center justify-center min-h-screen">

  <div class="bg-white shadow-md rounded-lg w-full max-w-md p-8">
    <h1 class="text-2xl font-extrabold text-center mb-6">Cadastrar Paragem</h1>

    <form action="api/paragens" method="POST" class="space-y-4">
      @csrf

      <!-- Nome -->
      <input 
        type="text" 
        name="nome" 
        placeholder="Nome*" 
        required 
        class="w-full px-4 py-2 border rounded focus:ring-2 focus:ring-green-400"
      >

      <!-- Latitude -->
      <input 
        type="text" 
        name="latitude" 
        placeholder="Latitude*" 
        required 
        class="w-full px-4 py-2 border rounded focus:ring-2 focus:ring-green-400"
      >

      <!-- Longitude -->
      <input 
        type="text" 
        name="longitude" 
        placeholder="Longitude*" 
        required 
        class="w-full px-4 py-2 border rounded focus:ring-2 focus:ring-green-400"
      >

      <!-- Categoria -->
      <select 
        name="id_categoria" 
        required 
        class="w-full px-4 py-2 border rounded focus:ring-2 focus:ring-green-400"
      >
        <option value="">Selecione uma categoria</option>
        @foreach($categoriasParagem as $categoria)
          <option value="{{ $categoria->id }}">{{ $categoria->nome }}</option>
        @endforeach
      </select>

      <!-- Botão -->
      <button 
        type="submit" 
        class="w-full bg-blue-500 text-white font-bold py-3 rounded hover:bg-blue-600 transition"
      >
        Salvar
      </button>
    </form>
  </div>

</body>
</html>
