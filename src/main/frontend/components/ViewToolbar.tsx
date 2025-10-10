import { PropsWithChildren } from 'react';
import { DrawerToggle } from '@vaadin/react-components';

export function Group(props: PropsWithChildren) {
  return <div className="flex flex-col items-stretch gap-s md:flex-row md:items-center">{props.children}</div>;
}

export type ViewToolbarProps = {
  title: string;
} & PropsWithChildren;

export function ViewToolbar(props: ViewToolbarProps) {
  return (
    <header className="flex flex-col justify-between items-stretch gap-m md:flex-row md:items-center">
      <div className="flex items-center">
        <DrawerToggle className="m-0" />
        <h1 className="text-xl m-0 font-light">{props.title}</h1>
      </div>
      {props.children && (
        <div className="flex flex-col justify-between flex-grow md:flex-row gap-s">{props.children}</div>
      )}
    </header>
  );
}
