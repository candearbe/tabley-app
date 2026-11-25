package com.uadeapps.tabley.di

import com.uadeapps.tabley.data.repository.CellRepositoryImpl
import com.uadeapps.tabley.data.repository.NotebookRepositoryImpl
import com.uadeapps.tabley.data.repository.TableRepositoryImpl
import com.uadeapps.tabley.domain.repository.CellRepository
import com.uadeapps.tabley.domain.repository.NotebookRepository
import com.uadeapps.tabley.domain.repository.TableRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.uadeapps.tabley.data.repository.ColumnRepositoryImpl
import com.uadeapps.tabley.data.repository.RowRepositoryImpl
import com.uadeapps.tabley.domain.repository.ColumnRepository
import com.uadeapps.tabley.domain.repository.RowRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNotebookRepository(
        implementation: NotebookRepositoryImpl
    ): NotebookRepository

    @Binds
    @Singleton
    abstract fun bindTableRepository(
        implementation: TableRepositoryImpl
    ): TableRepository

    @Binds
    @Singleton
    abstract fun bindCellRepository(
        implementation: CellRepositoryImpl
    ): CellRepository

    @Binds
    @Singleton
    abstract fun bindColumnRepository(
        implementation: ColumnRepositoryImpl
    ): ColumnRepository

    @Binds
    @Singleton
    abstract fun bindRowRepository(
        implementation: RowRepositoryImpl
    ): RowRepository
}
