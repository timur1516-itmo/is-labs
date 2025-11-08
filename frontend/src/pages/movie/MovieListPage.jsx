// MovieListPage.jsx
import React, {useEffect, useRef, useState} from 'react'
import {Card} from 'primereact/card'
import {Button} from 'primereact/button'
import {ConfirmDialog, confirmDialog} from 'primereact/confirmdialog'
import {Toast} from 'primereact/toast'
import {Dialog} from 'primereact/dialog'

import MovieTable from '../../components/movie/MovieTable.jsx'
import MovieForm from '../../components/movie/MovieForm.jsx'
import MovieService from '../../services/MovieService.jsx'
import ImportService from '../../services/ImportService.jsx'

const MovieListPage = () => {
    const [movies, setMovies] = useState([])
    const [loading, setLoading] = useState(false)
    const [totalRecords, setTotalRecords] = useState(100)
    const [lazyState, setLazyState] = useState({
        first: 0,
        rows: 10,
        sortField: null,
        sortOrder: null,
        filters: {}
    });
    const [selectedMovie, setSelectedMovie] = useState(null)
    const [createDialogVisible, setCreateDialogVisible] = useState(false)
    const [editDialogVisible, setEditDialogVisible] = useState(false)
    const toast = useRef(null)
    const fileInputRef = useRef(null)

    useEffect(() => {
        loadMovies()
    }, [lazyState])

    const loadMovies = async () => {
        setLoading(true)
        try {
            const response = await MovieService.getMovies(lazyState)
            setMovies(response.data.data)
            setTotalRecords(response.data.totalRecords)
        } catch (error) {
            toast.current.show({severity: 'error', summary: 'Ошибка', detail: 'Не удалось загрузить фильмы'})
        } finally {
            setLoading(false)
        }
    }

    const handleCreate = () => {
        setCreateDialogVisible(true)
    }

    const handleEdit = () => {
        if (selectedMovie) {
            setEditDialogVisible(true)
        }
    }

    const handleDelete = () => {
        if (selectedMovie) {
            confirmDialog({
                message: 'Вы уверены, что хотите удалить этот фильм?',
                header: 'Подтверждение удаления',
                icon: 'pi pi-exclamation-triangle',
                accept: () => deleteMovie(selectedMovie.id)
            })
        }
    }

    const handleImport = () => {
        fileInputRef.current.click();
    }

    const handleFileChange = async (event) => {
        const file = event.target.files[0];
        if (file) {
            if (file.type !== 'application/json') {
                toast.current.show({
                    severity: 'error',
                    summary: 'Ошибка',
                    detail: 'Поддерживаются только JSON файлы'
                });
                return;
            }

            const formData = new FormData();
            formData.append('file', file);

            try {
                await ImportService.importMovies(formData);
                loadMovies();
                toast.current.show({severity: 'success', summary: 'Успех', detail: 'Фильмы импортированы'});
            } catch (error) {
                toast.current.show({
                    severity: 'error',
                    summary: 'Ошибка',
                    detail: error.response?.data?.message || 'Не удалось импортировать фильмы'
                });
            } finally {
                event.target.value = null; // Сбросить значение инпута
            }
        }
    };

    const deleteMovie = async (id) => {
        try {
            await MovieService.deleteMovie(id)
            loadMovies()
            setSelectedMovie(null)
            toast.current.show({severity: 'success', summary: 'Успех', detail: 'Фильм удален'})
        } catch (error) {
            toast.current.show({
                severity: 'error',
                summary: 'Ошибка',
                detail: error.response?.data?.message || 'Не удалось удалить фильм'
            })
        }
    }

    const onCreateSuccess = async (movieData) => {
        try {
            await MovieService.createMovie(movieData)
            setCreateDialogVisible(false)
            loadMovies()
            toast.current.show({severity: 'success', summary: 'Успех', detail: 'Фильм создан'})
        } catch (error) {
            toast.current.show({
                severity: 'error',
                summary: 'Ошибка',
                detail: error.response?.data?.message || 'Не удалось создать фильм'
            })
        }
    }

    const onEditSuccess = async (movieData) => {
        try {
            await MovieService.updateMovie(selectedMovie.id, movieData)
            setEditDialogVisible(false)
            loadMovies()
            toast.current.show({severity: 'success', summary: 'Успех', detail: 'Фильм обновлен'})
        } catch (error) {
            console.error(error)
            toast.current.show({
                severity: 'error',
                summary: 'Ошибка',
                detail: error.response?.data?.message || 'Не удалось обновить фильм'
            })
        }
    }

    const handleSelectionChange = (e) => {
        setSelectedMovie(e.value)
    }

    const handleLazyStateChange = (e) => {
        setLazyState(e);
    }

    return (
        <div>
            <Toast ref={toast}/>
            <ConfirmDialog/>

            <input
                type="file"
                ref={fileInputRef}
                style={{display: 'none'}}
                accept=".json"
                onChange={handleFileChange}
            />

            {/* Диалог создания */}
            <Dialog
                header="Создать фильм"
                visible={createDialogVisible}
                style={{width: '50vw'}}
                onHide={() => setCreateDialogVisible(false)}
            >
                <MovieForm
                    onSubmit={onCreateSuccess}
                    onCancel={() => setCreateDialogVisible(false)}
                    loading={loading}
                    toast={toast}
                />
            </Dialog>

            {/* Диалог редактирования */}
            <Dialog
                header="Редактировать фильм"
                visible={editDialogVisible}
                style={{width: '50vw'}}
                onHide={() => setEditDialogVisible(false)}
            >
                <MovieForm
                    movie={selectedMovie}
                    onSubmit={onEditSuccess}
                    onCancel={() => setEditDialogVisible(false)}
                />
            </Dialog>

            <Card title="Список фильмов">
                <div className="mb-3">
                    <Button label="Создать" icon="pi pi-plus" onClick={handleCreate}/>
                    <Button
                        label="Импорт из файла"
                        icon="pi pi-upload"
                        onClick={handleImport}
                        className="ml-2"
                    />
                    <Button
                        label="Изменить"
                        icon="pi pi-pencil"
                        onClick={handleEdit}
                        className="ml-2"
                        disabled={!selectedMovie}
                    />
                    <Button
                        label="Удалить"
                        icon="pi pi-trash"
                        onClick={handleDelete}
                        className="ml-2 p-button-danger"
                        disabled={!selectedMovie}
                    />
                </div>

                <MovieTable
                    movies={movies}
                    loading={loading}
                    lazyState={lazyState}
                    setLazyState={setLazyState}
                    totalRecords={totalRecords}
                    selection={selectedMovie}
                    onSelectionChange={handleSelectionChange}
                />
            </Card>
        </div>
    )
}

export default MovieListPage