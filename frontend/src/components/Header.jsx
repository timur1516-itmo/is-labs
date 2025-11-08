import React from 'react'
import {Link} from 'react-router-dom'
import {Menubar} from 'primereact/menubar'

const Header = () => {
    const items = [
        {
            label: 'Фильмы',
            icon: 'pi pi-video',
            template: (item) => (
                <Link to="/movies" className="p-menuitem-link">
                    <span className={item.icon}></span>
                    <span className="mx-2">{item.label}</span>
                </Link>
            )
        },
        {
            label: 'Люди',
            icon: 'pi pi-users',
            template: (item) => (
                <Link to="/people" className="p-menuitem-link">
                    <span className={item.icon}></span>
                    <span className="mx-2">{item.label}</span>
                </Link>
            )
        },
        {
            label: 'Операции импорта',
            icon: 'pi pi-download',
            template: (item) => (
                <Link to="/import-operations" className="p-menuitem-link">
                    <span className={item.icon}></span>
                    <span className="mx-2">{item.label}</span>
                </Link>
            )
        },
        {
            label: 'Специальные операции',
            icon: 'pi pi-cog',
            template: (item) => (
                <Link to="/special-operations" className="p-menuitem-link">
                    <span className={item.icon}></span>
                    <span className="mx-2">{item.label}</span>
                </Link>
            )
        }
    ]

    return (
        <div className="card">
            <Menubar model={items}/>
        </div>
    )
}

export default Header