import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IConfigVariables, ConfigVariables } from '../config-variables.model';
import { ConfigVariablesService } from '../service/config-variables.service';

@Component({
  selector: 'jhi-config-variables-update',
  templateUrl: './config-variables-update.component.html',
})
export class ConfigVariablesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    configVarLong1: [],
    configVarLong2: [],
    configVarLong3: [],
    configVarLong4: [],
    configVarLong5: [],
    configVarLong6: [],
    configVarLong7: [],
    configVarLong8: [],
    configVarLong9: [],
    configVarLong10: [],
    configVarLong11: [],
    configVarLong12: [],
    configVarLong13: [],
    configVarLong14: [],
    configVarLong15: [],
    configVarBoolean16: [],
    configVarBoolean17: [],
    configVarBoolean18: [],
    configVarString19: [],
    configVarString20: [],
  });

  constructor(
    protected configVariablesService: ConfigVariablesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configVariables }) => {
      this.updateForm(configVariables);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configVariables = this.createFromForm();
    if (configVariables.id !== undefined) {
      this.subscribeToSaveResponse(this.configVariablesService.update(configVariables));
    } else {
      this.subscribeToSaveResponse(this.configVariablesService.create(configVariables));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigVariables>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(configVariables: IConfigVariables): void {
    this.editForm.patchValue({
      id: configVariables.id,
      configVarLong1: configVariables.configVarLong1,
      configVarLong2: configVariables.configVarLong2,
      configVarLong3: configVariables.configVarLong3,
      configVarLong4: configVariables.configVarLong4,
      configVarLong5: configVariables.configVarLong5,
      configVarLong6: configVariables.configVarLong6,
      configVarLong7: configVariables.configVarLong7,
      configVarLong8: configVariables.configVarLong8,
      configVarLong9: configVariables.configVarLong9,
      configVarLong10: configVariables.configVarLong10,
      configVarLong11: configVariables.configVarLong11,
      configVarLong12: configVariables.configVarLong12,
      configVarLong13: configVariables.configVarLong13,
      configVarLong14: configVariables.configVarLong14,
      configVarLong15: configVariables.configVarLong15,
      configVarBoolean16: configVariables.configVarBoolean16,
      configVarBoolean17: configVariables.configVarBoolean17,
      configVarBoolean18: configVariables.configVarBoolean18,
      configVarString19: configVariables.configVarString19,
      configVarString20: configVariables.configVarString20,
    });
  }

  protected createFromForm(): IConfigVariables {
    return {
      ...new ConfigVariables(),
      id: this.editForm.get(['id'])!.value,
      configVarLong1: this.editForm.get(['configVarLong1'])!.value,
      configVarLong2: this.editForm.get(['configVarLong2'])!.value,
      configVarLong3: this.editForm.get(['configVarLong3'])!.value,
      configVarLong4: this.editForm.get(['configVarLong4'])!.value,
      configVarLong5: this.editForm.get(['configVarLong5'])!.value,
      configVarLong6: this.editForm.get(['configVarLong6'])!.value,
      configVarLong7: this.editForm.get(['configVarLong7'])!.value,
      configVarLong8: this.editForm.get(['configVarLong8'])!.value,
      configVarLong9: this.editForm.get(['configVarLong9'])!.value,
      configVarLong10: this.editForm.get(['configVarLong10'])!.value,
      configVarLong11: this.editForm.get(['configVarLong11'])!.value,
      configVarLong12: this.editForm.get(['configVarLong12'])!.value,
      configVarLong13: this.editForm.get(['configVarLong13'])!.value,
      configVarLong14: this.editForm.get(['configVarLong14'])!.value,
      configVarLong15: this.editForm.get(['configVarLong15'])!.value,
      configVarBoolean16: this.editForm.get(['configVarBoolean16'])!.value,
      configVarBoolean17: this.editForm.get(['configVarBoolean17'])!.value,
      configVarBoolean18: this.editForm.get(['configVarBoolean18'])!.value,
      configVarString19: this.editForm.get(['configVarString19'])!.value,
      configVarString20: this.editForm.get(['configVarString20'])!.value,
    };
  }
}
