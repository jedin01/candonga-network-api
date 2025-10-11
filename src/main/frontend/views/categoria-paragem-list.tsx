import { ViewConfig } from "@vaadin/hilla-file-router/types.js";
import {
  Button,
  Grid,
  GridColumn,
  TextField,
  TextArea,
  Dialog,
  HorizontalLayout,
  VerticalLayout,
  FormLayout,
  Card,
} from "@vaadin/react-components";
import { Notification } from "@vaadin/react-components/Notification";
import { CategoriaParagemService } from "Frontend/generated/endpoints";
import { useSignal } from "@vaadin/hilla-react-signals";
import handleError from "Frontend/views/_ErrorHandler";
import { Group, ViewToolbar } from "Frontend/components/ViewToolbar";
import { useEffect } from "react";

export const config: ViewConfig = {
  title: "Categoria de Paragem",
  menu: {
    icon: "vaadin:tags",
    order: 2,
    title: "Categorias de Paragem",
  },
};

interface CategoriaParagem {
  id?: number;
  nome: string;
  descricao?: string;
}

export default function CategoriaParagemListView() {
  const categorias = useSignal<CategoriaParagem[]>([]);
  const loading = useSignal(false);
  const dialogOpen = useSignal(false);
  const editingCategoria = useSignal<CategoriaParagem | null>(null);
  const searchTerm = useSignal("");
  const statistics = useSignal<any[]>([]);

  // Form fields
  const nome = useSignal("");
  const descricao = useSignal("");

  const loadCategorias = async () => {
    loading.value = true;
    try {
      const result = await CategoriaParagemService.listarTodas();
      categorias.value = result;
    } catch (error) {
      handleError(error);
    } finally {
      loading.value = false;
    }
  };

  const loadStatistics = async () => {
    try {
      const stats = await CategoriaParagemService.obterEstatisticas();
      statistics.value = stats;
    } catch (error) {
      console.error("Error loading statistics:", error);
    }
  };

  useEffect(() => {
    loadCategorias();
    loadStatistics();
  }, []);

  const openCreateDialog = () => {
    editingCategoria.value = null;
    nome.value = "";
    descricao.value = "";
    dialogOpen.value = true;
  };

  const openEditDialog = (categoria: CategoriaParagem) => {
    editingCategoria.value = categoria;
    nome.value = categoria.nome;
    descricao.value = categoria.descricao || "";
    dialogOpen.value = true;
  };

  const closeDialog = () => {
    dialogOpen.value = false;
    editingCategoria.value = null;
    nome.value = "";
    descricao.value = "";
  };

  const saveCategoria = async () => {
    if (!nome.value.trim()) {
      Notification.show("Nome é obrigatório", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
      return;
    }

    try {
      const categoriaData = {
        nome: nome.value.trim(),
        descricao: descricao.value.trim() || undefined,
      };

      if (editingCategoria.value) {
        await CategoriaParagemService.atualizar(editingCategoria.value.id!, categoriaData);
        Notification.show("Categoria atualizada com sucesso", {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      } else {
        await CategoriaParagemService.criar(categoriaData);
        Notification.show("Categoria criada com sucesso", {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      }

      closeDialog();
      loadCategorias();
      loadStatistics();
    } catch (error) {
      handleError(error);
    }
  };

  const deleteCategoria = async (categoria: CategoriaParagem) => {
    if (!confirm(`Tem certeza que deseja excluir a categoria "${categoria.nome}"?`)) {
      return;
    }

    try {
      await CategoriaParagemService.deletar(categoria.id!);
      Notification.show("Categoria excluída com sucesso", {
        duration: 3000,
        position: "bottom-end",
        theme: "success",
      });
      loadCategorias();
      loadStatistics();
    } catch (error) {
      handleError(error);
    }
  };

  const searchCategorias = async () => {
    if (searchTerm.value.trim()) {
      try {
        const results = await CategoriaParagemService.buscar(searchTerm.value);
        categorias.value = results;
      } catch (error) {
        handleError(error);
      }
    } else {
      loadCategorias();
    }
  };

  const clearSearch = () => {
    searchTerm.value = "";
    loadCategorias();
  };

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Gestão de Categorias de Paragem">
        <Group>
          <Button onClick={openCreateDialog} theme="primary">
            Nova Categoria
          </Button>
        </Group>
      </ViewToolbar>

      {/* Statistics */}
      {statistics.value.length > 0 && (
        <Card>
          <VerticalLayout theme="spacing padding">
            <h3>Estatísticas por Categoria</h3>
            <HorizontalLayout theme="spacing">
              {statistics.value.map((stat, index) => (
                <div key={index}>
                  <strong>{stat.nomeCategoria}:</strong> {stat.quantidadeParagens} paragens
                </div>
              ))}
            </HorizontalLayout>
          </VerticalLayout>
        </Card>
      )}

      {/* Search */}
      <Card>
        <HorizontalLayout theme="spacing padding">
          <TextField
            placeholder="Pesquisar categorias..."
            value={searchTerm.value}
            onValueChanged={(e) => (searchTerm.value = e.detail.value)}
            style={{ minWidth: "25em" }}
          />
          <Button onClick={searchCategorias} theme="secondary">
            Buscar
          </Button>
          <Button onClick={clearSearch} theme="tertiary">
            Limpar
          </Button>
        </HorizontalLayout>
      </Card>

      {/* Grid */}
      <Card style={{ flexGrow: 1 }}>
        <Grid items={categorias.value} style={{ height: "100%" }}>
          <GridColumn path="id" header="ID" width="80px" flexGrow={0} />

          <GridColumn path="nome" header="Nome" flexGrow={2}>
            {({ item }) => <strong>{item.nome}</strong>}
          </GridColumn>

          <GridColumn path="descricao" header="Descrição" flexGrow={3}>
            {({ item }) => (
              <span style={{ color: item.descricao ? "inherit" : "#999" }}>
                {item.descricao || "Sem descrição"}
              </span>
            )}
          </GridColumn>

          <GridColumn header="Ações" width="200px" flexGrow={0}>
            {({ item }) => (
              <HorizontalLayout theme="spacing">
                <Button
                  theme="primary tertiary small"
                  onClick={() => openEditDialog(item)}
                >
                  Editar
                </Button>
                <Button
                  theme="error tertiary small"
                  onClick={() => deleteCategoria(item)}
                >
                  Excluir
                </Button>
              </HorizontalLayout>
            )}
          </GridColumn>
        </Grid>
      </Card>

      {/* Dialog */}
      <Dialog
        opened={dialogOpen.value}
        onOpenedChanged={(e) => {
          if (!e.detail.value) closeDialog();
        }}
        headerTitle={editingCategoria.value ? "Editar Categoria" : "Nova Categoria"}
      >
        <VerticalLayout theme="spacing" style={{ width: "400px" }}>
          <FormLayout>
            <TextField
              label="Nome"
              value={nome.value}
              onValueChanged={(e) => (nome.value = e.detail.value)}
              required
              maxlength={100}
              helperText="Máximo 100 caracteres"
            />

            <TextArea
              label="Descrição"
              value={descricao.value}
              onValueChanged={(e) => (descricao.value = e.detail.value)}
              maxlength={255}
              helperText="Máximo 255 caracteres (opcional)"
            />
          </FormLayout>

          <HorizontalLayout theme="spacing" style={{ justifyContent: "flex-end" }}>
            <Button onClick={closeDialog} theme="tertiary">
              Cancelar
            </Button>
            <Button onClick={saveCategoria} theme="primary">
              {editingCategoria.value ? "Atualizar" : "Criar"}
            </Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
    </main>
  );
}
