import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, DatePicker, Grid, GridColumn, TextField } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useGridDataProvider } from '@vaadin/hilla-react-crud';

export const config: ViewConfig = {
  title: 'Task List',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Task List',
  },
};

const dateTimeFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: 'medium',
  timeStyle: 'medium',
});

const dateFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: 'medium',
});

type TaskEntryFormProps = {
  onTaskCreated?: () => void;
};

function TaskEntryForm(props: TaskEntryFormProps) {
  const description = useSignal('');
  const dueDate = useSignal<string | undefined>('');
  const createTask = async () => {
    try {
      await TaskService.createTask(description.value, dueDate.value);
      if (props.onTaskCreated) {
        props.onTaskCreated();
      }
      description.value = '';
      dueDate.value = undefined;
      Notification.show('Task added', { duration: 3000, position: 'bottom-end', theme: 'success' });
    } catch (error) {
      handleError(error);
    }
  };
  return (
    <>
      <TextField
        placeholder="What do you want to do?"
        aria-label="Task description"
        maxlength={255}
        style={{ minWidth: '20em' }}
        value={description.value}
        onValueChanged={(evt) => (description.value = evt.detail.value)}
      />
      <DatePicker
        placeholder="Due date"
        aria-label="Due date"
        value={dueDate.value}
        onValueChanged={(evt) => (dueDate.value = evt.detail.value)}
      />
      <Button onClick={createTask} theme="primary">
        Create
      </Button>
    </>
  );
}

export default function TaskListView() {
  const dataProvider = useGridDataProvider(TaskService.list);

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Task List">
        <Group>
          <TaskEntryForm onTaskCreated={dataProvider.refresh} />
        </Group>
      </ViewToolbar>
      <Grid dataProvider={dataProvider}>
        <GridColumn path="description" />
        <GridColumn path="dueDate" header="Due Date">
          {({ item }) => (item.dueDate ? dateFormatter.format(new Date(item.dueDate)) : 'Never')}
        </GridColumn>
        <GridColumn path="creationDate" header="Creation Date">
          {({ item }) => dateTimeFormatter.format(new Date(item.creationDate))}
        </GridColumn>
      </Grid>
    </main>
  );
}
