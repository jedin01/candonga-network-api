// TODO Replace with your own main view.

import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Icon } from '@vaadin/react-components';
import { ViewToolbar } from 'Frontend/components/ViewToolbar';

export const config: ViewConfig = {
  menu: {
    icon: 'vaadin:home',
    order: -100,
    title: 'Welcome!',
  },
};

export default function MainView() {
  return (
    <main className="p-m flex flex-col box-border w-full h-full">
      <ViewToolbar title="Welcome to Vaadin!" />
      <div className="flex-grow flex flex-col items-center justify-center">
        <div className="flex flex-col items-center">
          <Icon src="icons/construction.svg" className="text-success" style={{ width: '200px', height: '200px' }} />
          <p>Replace this view with your own main view!</p>
        </div>
      </div>
    </main>
  );
}
