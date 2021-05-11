export interface IConfigVariables {
  id?: number;
  configVarLong1?: number | null;
  configVarLong2?: number | null;
  configVarLong3?: number | null;
  configVarLong4?: number | null;
  configVarLong5?: number | null;
  configVarLong6?: number | null;
  configVarLong7?: number | null;
  configVarLong8?: number | null;
  configVarLong9?: number | null;
  configVarLong10?: number | null;
  configVarLong11?: number | null;
  configVarLong12?: number | null;
  configVarLong13?: number | null;
  configVarLong14?: number | null;
  configVarLong15?: number | null;
  configVarBoolean16?: boolean | null;
  configVarBoolean17?: boolean | null;
  configVarBoolean18?: boolean | null;
  configVarString19?: string | null;
  configVarString20?: string | null;
}

export class ConfigVariables implements IConfigVariables {
  constructor(
    public id?: number,
    public configVarLong1?: number | null,
    public configVarLong2?: number | null,
    public configVarLong3?: number | null,
    public configVarLong4?: number | null,
    public configVarLong5?: number | null,
    public configVarLong6?: number | null,
    public configVarLong7?: number | null,
    public configVarLong8?: number | null,
    public configVarLong9?: number | null,
    public configVarLong10?: number | null,
    public configVarLong11?: number | null,
    public configVarLong12?: number | null,
    public configVarLong13?: number | null,
    public configVarLong14?: number | null,
    public configVarLong15?: number | null,
    public configVarBoolean16?: boolean | null,
    public configVarBoolean17?: boolean | null,
    public configVarBoolean18?: boolean | null,
    public configVarString19?: string | null,
    public configVarString20?: string | null
  ) {
    this.configVarBoolean16 = this.configVarBoolean16 ?? false;
    this.configVarBoolean17 = this.configVarBoolean17 ?? false;
    this.configVarBoolean18 = this.configVarBoolean18 ?? false;
  }
}

export function getConfigVariablesIdentifier(configVariables: IConfigVariables): number | undefined {
  return configVariables.id;
}
